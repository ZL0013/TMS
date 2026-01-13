package com.xie.web.controller.system;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "岗位信息管理")
@RestController
@RequestMapping("/v1/api/posts")
@RequiredArgsConstructor
public class SysPostController {
}
