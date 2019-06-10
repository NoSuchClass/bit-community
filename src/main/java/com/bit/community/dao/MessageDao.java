package com.bit.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import com.bit.community.model.Message;

@Mapper
public interface MessageDao {
	String TABLE_NAME = "message";
	String INSERT_FIELDS = "from_id, to_id, content, conversation_id, created_date, has_read";
	String SELECT_FIELDS = "id, " + INSERT_FIELDS;

	@Insert({ "insert into " + TABLE_NAME + " (" + INSERT_FIELDS
			+ ") values( #{fromId}, #{toId}, #{content}, #{conversationId}, #{createdDate}, #{hasRead})" })
	public int insertMessage(Message message);

	@Select({ "select " + SELECT_FIELDS + " from " + TABLE_NAME
			+ " where conversation_id = #{conversationId} order by created_date desc limit #{offset}, #{limit}" })
	public List<Message> getMessageDetail(@Param("conversationId") String conversationId, @Param("offset") int offset,
			@Param("limit") int limit);

	// SELECT tt.* ,COUNT(id) FROM
	// ( SELECT * FROM message WHERE to_id=28 OR from_id=28 ORDER BY created_date
	// DESC LIMIT 0,10000) AS tt
	// GROUP BY tt.conversation_id
	// 视频中说的是可以直接使用count(id)查询消息条数，然后存到id中，但是现在不行
	@Select({ "select " + INSERT_FIELDS + ",count(id) as id from (SELECT * FROM " + TABLE_NAME
			+ " where from_id = #{userId} or to_id = #{userId} ORDER BY created_date DESC limit 0, 1000) "
			+ "as tt GROUP BY conversation_id ORDER BY created_date DESC limit #{offset}, #{limit} " })
	public List<Message> getMessageByUserId(@Param("userId") int userId, @Param("offset") int offset,
			@Param("limit") int limit);

	@Select({ "select count(id) from " + TABLE_NAME
			+ " where conversation_id =#{conversationId} and to_id=#{userId} and has_read=0" })
	public int getMessageUnreadCount(int userId, String conversationId);

	@Update({ "update " + TABLE_NAME + " set has_read=1 where conversation_id=#{conversationId} and has_read=0" })
	public int changeReadStatus(String conversationId);
}
