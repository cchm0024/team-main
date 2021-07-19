package org.team.mapper.member;

import org.team.domain.member.AuthVO;
import org.team.domain.member.MemberVO;


public interface MemberMapper {
	
	public int insert(MemberVO vo);
	
	public int insertAuth(AuthVO vo);
	
	public MemberVO read(String userid);
	
	public int update(MemberVO vo);

	public int remove(MemberVO vo);
	
	public int removeAuth(MemberVO vo);
}
