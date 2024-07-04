package com.qin.catcat.unite.controller;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.apache.tomcat.util.openssl.pem_password_cb;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qin.catcat.unite.common.result.Result;
import com.qin.catcat.unite.common.utils.GeneratorIdUtil;
import com.qin.catcat.unite.common.utils.JwtTokenProvider;
import com.qin.catcat.unite.popo.dto.PostDTO;
import com.qin.catcat.unite.popo.dto.UpdatePostDTO;
import com.qin.catcat.unite.popo.entity.Post;
import com.qin.catcat.unite.service.PostService;
import com.qin.catcat.unite.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


//帖子控制器
@RestController
@RequestMapping("/post")
@Tag(name = "帖子接口")
@Slf4j
public class PostController {
    @Autowired PostService postService;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired UserService userService;
    @Autowired GeneratorIdUtil generatorIdUtil;
    /**
    * 新增帖子
    * @param 
    * @return 
    */
    @PostMapping("/addpost")
    public Result<?> addPost(@RequestHeader("Authorization") String Token,@RequestBody PostDTO postDTO) {
        String username = jwtTokenProvider.getUsernameFromToken(Token);
        String userId = jwtTokenProvider.getUserIdFromJWT(Token);
        log.info("用户{}请求新增帖子",username);

        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);//属性拷贝DTO to entity
        String userNickname = userService.getNicknameFromId(userId); //根据用户ID查询用户昵称

        post.setPostId(Long.parseLong(generatorIdUtil.GeneratorRandomId()));//设置帖子ID
        post.setLikeCount(0);//设置点赞数 初始化0
        post.setAuthorId(Long.parseLong(userId));//设置作者ID（即用户本ID）
        post.setAuthorNickname(userNickname);//设置作者昵称
        post.setCommentCount(0);//设置评论数 初始化0
        post.setSendTime(Timestamp.from(Instant.now()));//设置发帖时间

        Boolean signal = postService.add(post);
        return Result.success();
    }

    /**
    * 删除帖子
    * @param 
    * @return 
    */
    @DeleteMapping("/deletepost")
    public Result<?> deletePost(@RequestHeader("Authorization") String Token,@RequestParam String postId){
        String username = jwtTokenProvider.getUsernameFromToken(Token);
        log.info("用户{}请求删除帖子{}",username,postId);

        Boolean signal = postService.delete(postId);
        if(!signal){
            return Result.error("删除帖子失败");
        }
        return Result.success();
    }

    /**
    * 更新帖子
    * @param 
    * @return 
    */
    @PutMapping("/updatepost")
    public Result<?> updatePost(@RequestHeader("Authorization") String Token,@RequestBody UpdatePostDTO updatePostDTO){
        String username = jwtTokenProvider.getUsernameFromToken(Token);
        log.info("用户{}请求更新帖子{}",username,updatePostDTO.getPostId());

        Post post = postService.getPostByPostId(String.valueOf(updatePostDTO.getPostId()));//根据帖子ID查询帖子
        post.setArticle(updatePostDTO.getArticle());//更新文章
        post.setTitle(updatePostDTO.getTitle());//更新标题
        post.setUpdateTime(Timestamp.from(Instant.now()));//更新更新时间
        int signal = postService.update(post);
        if(signal!=1){
            return Result.error("更新失败");
        }else{
            return Result.success();
        }
    }


    /**
    * 查询全部帖子
    * @param 
    * @return 
    */
    @GetMapping("/getAllPost")
    public Result<List<Post>> getAllPost(@RequestHeader("Authorization") String Token){
        String username = jwtTokenProvider.getUsernameFromToken(Token);
        log.info("用户{}请求查询全部帖子",username);

        List<Post> posts = postService.getAllPost();
        return Result.success(posts);
    }

    /**
    * 根据帖子ID查询帖子
    * @param 
    * @return 
    */
    @GetMapping("/getPostByPostid")
    public Result<Post> getPostByPostid(@RequestHeader("Authorization") String Token, @RequestParam String postId){
        String username = jwtTokenProvider.getUsernameFromToken(Token);
        log.info("用户{}请求根据帖子ID查询帖子{}",username,postId);

        Post post = postService.getPostByPostId(postId);
        return Result.success(post);
    }

    /**
    * 根据发布时间分页查询前十条帖子
    * @param 
    * @return 
    */
    @GetMapping("/getPostBySendtimeForPage")
    public Result<IPage<Post>> getPostBySendtime(@RequestHeader("Authorization") String Token, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue="10") int pageSize){
        String username = jwtTokenProvider.getUsernameFromToken(Token);
        log.info("用户{}请求根据发布时间分页查询前十条帖子,第{}页，每页{}条",username,page,pageSize);

        IPage<Post> posts = postService.getPostBySendtime(page,pageSize);
        return Result.success(posts);
    }

    /**
    * 根据点赞数分页查询前十条帖子
    * @param 
    * @return 
    */

    /**
    * 根据标题搜索相关帖子（匹配标题、匹配文章内容）
    * @param 
    * @return 
    */

    /**
    * 根据作者昵称搜索相关帖子
    * @param 
    * @return 
    */
}
