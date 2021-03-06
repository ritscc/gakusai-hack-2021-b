package dev.abelab.gifttree.db.mapper.base;

import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.db.entity.UserGiftExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGiftBaseMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    long countByExample(UserGiftExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int deleteByExample(UserGiftExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(@Param("userId") Integer userId, @Param("giftId") Integer giftId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int insert(UserGift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int insertSelective(UserGift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    List<UserGift> selectByExample(UserGiftExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    UserGift selectByPrimaryKey(@Param("userId") Integer userId, @Param("giftId") Integer giftId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") UserGift record, @Param("example") UserGiftExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") UserGift record, @Param("example") UserGiftExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(UserGift record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_gift
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(UserGift record);
}