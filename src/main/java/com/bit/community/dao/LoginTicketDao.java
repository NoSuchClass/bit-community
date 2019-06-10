package com.bit.community.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import com.bit.community.model.LoginTicket;

@Mapper
public interface LoginTicketDao {
	String TABLE_NAME = "login_ticket";
	String INSERT_FIELDS = "user_id, ticket, expired, status";
	String SELECT_FIELDS = "id, "+INSERT_FIELDS;
	
	@Insert({"insert into "+TABLE_NAME+" ( "+INSERT_FIELDS +" )" + " VALUES(#{userId},#{ticket},#{expired},#{status})"})
	public int addTicket(LoginTicket loginTicket);
	
	@Select("select "+INSERT_FIELDS+" from "+TABLE_NAME+" where ticket=#{ticket}")
	public LoginTicket findTicketByName(String ticket);
	
	@Update("update "+TABLE_NAME+" set status=#{status} where ticket=#{ticket}")
	public boolean updateStatus(@Param("ticket") String ticket, @Param("status") Integer status);
}
