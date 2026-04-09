package com.chuwa.redbook.service.impl;

import com.chuwa.redbook.dao.CommentRepository;
import com.chuwa.redbook.dao.PostRepository;
import com.chuwa.redbook.entity.Comment;
import com.chuwa.redbook.entity.Post;
import com.chuwa.redbook.exception.BlogAPIException;
import com.chuwa.redbook.exception.ResourceNotFoundException;
import com.chuwa.redbook.payload.CommentDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImplTest.class);

    // ================= mocks =================
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock(name = "modelMapper")
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    // ================= test data =================
    private Post post;
    private Post otherPost;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {

        logger.info("set up CommentService test");

        this.post = new Post(
                1L,
                "post-title",
                "post-description",
                "post-content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        this.otherPost = new Post(
                2L,
                "other-title",
                "other-description",
                "other-content",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        this.comment = new Comment(
                1L,
                "Tom",
                "tom@test.com",
                "hello world"
        );
        this.comment.setPost(post);

        this.commentDto = new CommentDto(
                1L,
                "Tom",
                "tom@test.com",
                "hello world"
        );
    }

    // ================= createComment =================

    @Test
    void testCreateComment_success() {

        Mockito.when(modelMapper.map(Mockito.any(CommentDto.class), Mockito.eq(Comment.class)))
                .thenReturn(comment);

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito.when(modelMapper.map(Mockito.any(Comment.class), Mockito.eq(CommentDto.class)))
                .thenReturn(commentDto);

        CommentDto result = commentService.createComment(1L, commentDto);

        assertEquals(commentDto.getName(), result.getName());
        assertEquals(commentDto.getEmail(), result.getEmail());
        assertEquals(commentDto.getBody(), result.getBody());

        Mockito.verify(commentRepository, Mockito.times(1))
                .save(Mockito.any(Comment.class));
    }

    @Test
    void testCreateComment_postNotFound() {

        Mockito.when(modelMapper.map(Mockito.any(CommentDto.class), Mockito.eq(Comment.class)))
                .thenReturn(comment);

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.createComment(1L, commentDto));
    }

    // ================= getCommentsByPostId =================

    @Test
    void testGetCommentsByPostId() {

        List<Comment> comments = List.of(comment);

        Mockito.when(commentRepository.findByPostId(1L))
                .thenReturn(comments);

        Mockito.when(modelMapper.map(Mockito.any(Comment.class), Mockito.eq(CommentDto.class)))
                .thenReturn(commentDto);

        List<CommentDto> result = commentService.getCommentsByPostId(1L);

        assertEquals(1, result.size());
        assertEquals("Tom", result.get(0).getName());
    }

    // ================= getCommentById =================

    @Test
    void testGetCommentById_success() {

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        Mockito.when(modelMapper.map(Mockito.any(Comment.class), Mockito.eq(CommentDto.class)))
                .thenReturn(commentDto);

        CommentDto result = commentService.getCommentById(1L, 1L);

        assertNotNull(result);
        assertEquals(commentDto.getName(), result.getName());
    }

    @Test
    void testGetCommentById_postNotFound() {

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 1L));
    }

    @Test
    void testGetCommentById_commentNotFound() {

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getCommentById(1L, 1L));
    }

    @Test
    void testGetCommentById_notBelongToPost() {

        Comment tempComment = new Comment(
                1L,
                "Tom",
                "tom@test.com",
                "hello world"
        );
        tempComment.setPost(otherPost);

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(tempComment));

        assertThrows(BlogAPIException.class,
                () -> commentService.getCommentById(1L, 1L));
    }

    // ================= updateComment =================

    @Test
    void testUpdateComment_success() {

        CommentDto updateDto = new CommentDto(
                1L,
                "Updated",
                "updated@test.com",
                "updated body"
        );

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);

        Mockito.when(modelMapper.map(Mockito.any(Comment.class), Mockito.eq(CommentDto.class)))
                .thenReturn(updateDto);

        CommentDto result = commentService.updateComment(1L, 1L, updateDto);

        assertEquals("Updated", result.getName());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("updated body", result.getBody());

        Mockito.verify(commentRepository, Mockito.times(1))
                .save(Mockito.any(Comment.class));
    }

    @Test
    void testUpdateComment_notBelongToPost() {

        Comment tempComment = new Comment(
                1L,
                "Tom",
                "tom@test.com",
                "hello world"
        );
        tempComment.setPost(otherPost);

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(tempComment));

        assertThrows(BlogAPIException.class,
                () -> commentService.updateComment(1L, 1L, commentDto));
    }

    @Test
    void testUpdateComment_partialUpdate() {
        CommentDto partial = new CommentDto();
        partial.setName("OnlyName");

        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(comment);
        Mockito.when(modelMapper.map(Mockito.any(Comment.class), Mockito.eq(CommentDto.class)))
                .thenReturn(partial);

        CommentDto result = commentService.updateComment(1L, 1L, partial);

        assertEquals("OnlyName", result.getName());
    }


    // ================= deleteComment =================

    @Test
    void testDeleteComment_success() {

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(comment));

        Mockito.doNothing().when(commentRepository).delete(Mockito.any(Comment.class));

        commentService.deleteComment(1L, 1L);

        Mockito.verify(commentRepository, Mockito.times(1))
                .delete(Mockito.any(Comment.class));
    }

    @Test
    void testDeleteComment_postNotFound() {

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 1L));

        Mockito.verify(commentRepository, Mockito.never())
                .delete(Mockito.any());
    }

    @Test
    void testDeleteComment_commentNotFound() {

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteComment(1L, 1L));

        Mockito.verify(commentRepository, Mockito.never())
                .delete(Mockito.any());
    }

    @Test
    void testDeleteComment_notBelongToPost() {

        Comment tempComment = new Comment(
                1L,
                "Tom",
                "tom@test.com",
                "hello world"
        );
        tempComment.setPost(otherPost);

        Mockito.when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        Mockito.when(commentRepository.findById(1L))
                .thenReturn(Optional.of(tempComment));

        assertThrows(BlogAPIException.class,
                () -> commentService.deleteComment(1L, 1L));

        Mockito.verify(commentRepository, Mockito.never())
                .delete(Mockito.any());
    }
}