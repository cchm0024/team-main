package org.team.service.main;

import java.util.List;

import org.team.domain.product.ProductCriteria;
import org.team.domain.product.ProductVO;

public interface MainService {

	public List<ProductVO> getList();
	
	public List<ProductVO> getSearchList(ProductCriteria cri);

	public List<ProductVO> getRank();
	
}
