package com.example.elice_3rd.counsel.controller;

import com.example.elice_3rd.counsel.dto.CounselRequestDto;
import com.example.elice_3rd.counsel.dto.CounselResponseDto;
import com.example.elice_3rd.counsel.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/counsels")
public class CounselAPIController {
    private final CounselService counselService;

    @PostMapping
    public ResponseEntity<Void> create(Principal principal, @RequestBody CounselRequestDto requestDto){
        counselService.create(principal.getName(), requestDto);
        // TODO create 이후 상담 목록 URI 설정
        return ResponseEntity.created(URI.create("/counsels")).build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(Principal principal, Long id, @RequestBody CounselRequestDto requestDto) {
        counselService.update(principal.getName(), id, requestDto);
        // TODO update 이후 상담 목록 URI 설정
        return ResponseEntity.ok().location(URI.create("/counsels")).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(Principal principal, Long id){
        counselService.delete(principal.getName(), id);
        return ResponseEntity.noContent().location(URI.create("/my-page")).build();
    }

    @GetMapping("detail")
    public ResponseEntity<CounselResponseDto> retrieveDetail(Long id){
        return ResponseEntity.ok(counselService.retrieveDetail(id));
    }

    @GetMapping
    public ResponseEntity<Page<CounselResponseDto>> retrieveAll(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(counselService.retrieveAll(pageable));
    }

    @GetMapping("/my-counsels")
    public ResponseEntity<Page<CounselResponseDto>> retrieveMyCounsels(Principal principal,
                                                                       @PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(counselService.retrieveMyCounsels(principal.getName(), pageable));
    }
}
