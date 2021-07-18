package org.team.mapper.product;

import java.util.List;

import org.team.domain.product.ProductVO;

public interface BookMapper {

	public List<ProductVO> getList();
	
	public ProductVO get(Long id);

	public int insert(ProductVO product);
}
