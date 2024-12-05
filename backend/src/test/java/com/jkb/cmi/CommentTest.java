package com.jkb.cmi;

import com.jkb.cmi.comment.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class CommentTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void test() {
        commentRepository.deleteById(1L);
    }

}
