package com.bit.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import com.bit.community.model.Comment;

@Mapper
public interface CommentDao {
	String TABLE_NAME = "comment";
	String INSERT_FIELDS = "content, created_date, entity_id, entity_type, user_id, status";
	String SELECT_FIELDS = "id, " + INSERT_FIELDS;

	@Insert({ "insert into " + TABLE_NAME + " ( " + INSERT_FIELDS + " )"
			+ "values (#{content},#{createdDate},#{entityId},#{entityType},#{userId},#{status})" })
	public int addComment(Comment comment);

	@Select({ "select * from " + TABLE_NAME
			+ " where entity_id = #{entityId} and entity_type = #{entityType} order by created_date desc" })
	public List<Comment> selectCommentById(int entityId, int entityType);

	@Select({
			"select count(id) from " + TABLE_NAME + " where entity_id = #{entityId} and entity_type = #{entityType} " })
	public int getCommentCount(int entityId, int entityType);

	@Update("update " + TABLE_NAME + " set status = #{status} where id = #{id}")
	public boolean deleteComment(@Param("id") int id, @Param("status") int status);

	@Select({ "select " + SELECT_FIELDS + " from " + TABLE_NAME + " where id=#{id}" })
	public Comment getCommentById(int id);

	@Select({ "select count(*) from " + TABLE_NAME + " where user_id=#{id}" })
	public int getUserCommentCount(Integer id);
}
