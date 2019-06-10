package com.bit.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import com.bit.community.model.Question;

@Mapper
public interface QuestionDao {
	String TABLE_NAME = "question";
	String INSERT_FIELDS = "title, content, created_date, user_id, comment_count";
	String SELECT_FIELDS = "id," + INSERT_FIELDS;

	@Insert({ "insert into " + TABLE_NAME + " (" + INSERT_FIELDS
			+ ") values(#{title}, #{content}, #{createdDate}, #{userId}, #{commentCount})" })
	int addQuestion(Question question);

	@Select({ "select " + SELECT_FIELDS + " from " + TABLE_NAME
			+ " ORDER BY created_date DESC limit #{offset}, #{limit}" })
	List<Question> selectLatestQuestion(@Param("offset") int offset, @Param("limit") int limit);

	@Select({ "select " + SELECT_FIELDS + " from " + TABLE_NAME
			+ " where user_id=#{userId} ORDER BY created_date DESC limit #{offset}, #{limit}" })
	List<Question> selectLatestQuestionById(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

	@Update({ "update " + TABLE_NAME + " set content=#{content} where id=#{id}" })
	boolean updateContent(Question question);

	@Delete({ "delete from " + TABLE_NAME + " where id = #{id}" })
	void deleteById(int id);

	@Select({ "select * from " + TABLE_NAME + " where id = #{qid}" })
	Question selectQuestionById(int qid);

	@Update({ " update " + TABLE_NAME + " set comment_count=#{count} where id=#{id}" })
	boolean updateCommentCount(@Param("id") int id, @Param("count") int count);
}
