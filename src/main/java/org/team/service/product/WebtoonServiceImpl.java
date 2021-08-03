package org.team.service.product;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.team.domain.product.CoverVO;
import org.team.domain.product.ProductFileVO;
import org.team.domain.product.ProductVO;
import org.team.mapper.product.ProductFileMapper;
import org.team.mapper.product.ProductReplyMapper;
import org.team.mapper.product.WebtoonMapper;

import lombok.Setter;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class WebtoonServiceImpl implements WebtoonService {
	
	private String bucketName;
	private String profileName;
	private S3Client s3;
	
	public WebtoonServiceImpl() {
		this.bucketName = "choongang-eemin90";
		this.profileName = "spring1";
		this.s3 = S3Client.builder()
				.credentialsProvider(ProfileCredentialsProvider.create(profileName))
				.build();
	}
	
	@Setter(onMethod_ = @Autowired)
	private WebtoonMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private ProductFileMapper fileMapper;
	
	@Setter(onMethod_ = @Autowired)
	private ProductReplyMapper replyMapper;

	@Override
	public List<ProductVO> getList() {
		return mapper.getList();
	}
	
	@Override
	public List<ProductVO> getRank() {
		return mapper.getRank();
	}
	
	@Override
	public String getCnt() {
		return mapper.getCnt();
	}
	
	@Override
	public List<ProductVO> getToday() {
		return mapper.getToday();
	}
	
	@Override
	public String getTodayCnt() {
		return mapper.getTodayCnt();
	}

	@Override
	public ProductVO get(Long id) {
		return mapper.get(id);
	}
	
	@Override
	public ProductVO getFile(Long id) {
		return mapper.readFile(id);
	}
	
	@Override
	public void plusCnt(Long id) {
		mapper.plusCnt(id);
	}

	@Override
	public void register(ProductVO product, MultipartFile file1, MultipartFile file2) {
		mapper.insert(product);
		
		if ((file1 != null && file1.getSize() > 0) && (file2 != null && file2.getSize() > 0)) {
			CoverVO vo1 = new CoverVO();
			vo1.setProduct_id(product.getId());
			vo1.setFile_name(file1.getOriginalFilename());
			
			ProductFileVO vo2 = new ProductFileVO();
			vo2.setProduct_id(product.getId());
			vo2.setFile_name(file2.getOriginalFilename());
			
			fileMapper.insertCover(vo1);
			uploadCover(product, file1);
			
			fileMapper.insertFile(vo2);
			uploadFile(product, file2);
		}
	}

	private void uploadCover(ProductVO product, MultipartFile file) {
		try (InputStream is = file.getInputStream()) {
			
			PutObjectRequest objectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key("webtoon/" + product.getId() + "/cover/" + file.getOriginalFilename()) // aws에 "webtoon/product id번호/cover/" 폴더에 파일 저장
					.contentType(file.getContentType())
					.acl(ObjectCannedACL.PUBLIC_READ)
					.build();
			
			s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void uploadFile(ProductVO product, MultipartFile file) {
		try (InputStream is = file.getInputStream()) {
			
			PutObjectRequest objectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key("webtoon/" + product.getId() + "/file/" + file.getOriginalFilename()) // aws에 "webtoon/product id번호/file/" 폴더에 파일 저장
					.contentType(file.getContentType())
					.acl(ObjectCannedACL.PUBLIC_READ)
					.build();
			
			s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean modify(ProductVO product, MultipartFile file1, MultipartFile file2) {
		if ((file1 != null && file1.getSize() > 0) && (file2 != null && file2.getSize() > 0)) {
			// aws에서 파일 삭제 후 재업로드
			ProductVO oldProduct1 = mapper.readCover(product.getId());
			ProductVO oldProduct2 = mapper.readFile(product.getId());
			removeCover(oldProduct1);
			removeFile(oldProduct2);
			uploadCover(product, file1);
			uploadFile(product, file2);
			
			// DB에서 삭제 후 다시 insert
			fileMapper.removeCoverById(product.getId());
			fileMapper.removeFileById(product.getId());
			
			CoverVO vo1 = new CoverVO();
			vo1.setProduct_id(product.getId());
			vo1.setFile_name(file1.getOriginalFilename());
			
			ProductFileVO vo2 = new ProductFileVO();
			vo2.setProduct_id(product.getId());
			vo2.setFile_name(file2.getOriginalFilename());
			
			fileMapper.insertCover(vo1);
			fileMapper.insertFile(vo2);
		} 
		
		return mapper.update(product) == 1;
	}

	private void removeCover(ProductVO product) {
		String key = "webtoon/" + product.getId() + "/cover/" + product.getFile_name();
		
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		
		s3.deleteObject(deleteObjectRequest);
	}
	
	private void removeFile(ProductVO product) {
		String key = "webtoon/" + product.getId() + "/file/" + product.getFile_name();
		
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(key)
				.build();
		
		s3.deleteObject(deleteObjectRequest);
	}

	@Override
	@Transactional
	public boolean remove(Long id) {
		// 댓글 삭제
		replyMapper.deleteDetailByPid(id);
		replyMapper.deleteByPid(id);
		
		// AWS에서 삭제
		ProductVO product1 = mapper.readCover(id);
		ProductVO product2 = mapper.readFile(id);
		removeCover(product1);
		removeFile(product2);
		
		// DB에서 삭제
		fileMapper.removeCoverById(id);
		fileMapper.removeFileById(id);
		
		// 책 삭제
		int cnt = mapper.remove(id);
		
		return cnt == 1;
	}

}
