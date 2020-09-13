package org.zerock.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mapper.Sample1Mapper;
import org.zerock.mapper.Sample2Mapper;

import lombok.Setter;

@Service
public class SampleTxServiceImpl implements SampleTxService {

	@Setter(onMethod_ = {@Autowired})
	private Sample1Mapper mapper1;
	
	@Setter(onMethod_ = {@Autowired})
	private Sample2Mapper mapper2;
	
	@Transactional	// 트랙잭션 처리, 예외 발생시 롤백이 되지 않음
	//@Transactional(propagation = Propagation.REQUIRES_NEW ,rollbackFor = {Exception.class})	// 예외 발생시 롤백
	@Override
	public void addData(String value) throws Exception {
		mapper1.insertCol1(value);
		mapper2.insertCol2(value);
	}

}
