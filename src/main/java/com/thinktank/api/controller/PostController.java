package com.thinktank.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinktank.api.dto.post.request.PostCreateDto;
import com.thinktank.api.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;

	@PostMapping("/post")
	public ResponseEntity<?> createPost(@RequestBody @Validated PostCreateDto postCreateDto) {
		postService.createPost(postCreateDto);
		return ResponseEntity.ok("OK");
	}

}
