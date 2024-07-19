package com.jkb.cmi.repository;

import com.jkb.cmi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c join fetch c.user where c.currency.market = :market")
    List<Comment> getByCurrency_Market(@Param("market") String market);
    @Override
    @Modifying(clearAutomatically = true)
    @Query("delete from Comment c where c.id = :id")
    void deleteById(@Param("id") Long id);
}
