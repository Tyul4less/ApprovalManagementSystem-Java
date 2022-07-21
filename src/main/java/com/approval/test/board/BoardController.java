package com.approval.test.board;

import com.approval.test.board.dto.request.*;
import com.approval.test.board.dto.response.BoardCommentResponse;
import com.approval.test.board.dto.response.BoardDetailResponse;
import com.approval.test.board.dto.response.BoardListResponse;
import com.approval.test.board.service.BoardService;
import com.approval.test.system.common.util.FileUtils;
import com.approval.test.system.common.util.dto.request.FileRequest;
import com.approval.test.system.common.util.dto.request.FileUploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
@CrossOrigin("*")
public class BoardController {

    private final BoardService boardService;

    //게시글
    static final String POST = "/post";
    static final String LIST = "/list";
    static final String INFO = "/info";
    static final String EDIT = "/edit";
    static final String DELETE = "/delete";
    //댓글
    static final String SAVE_COMMENT = "/comment/save";
    static final String GET_COMMENT = "/comment/list";
    static final String EDIT_COMMENT = "/comment/edit";
    static final String DELETE_COMMENT = "/comment/delete";
    //파일
    static final String FILE_UPLOAD = "/file/upload";
    static final String FILE_DOWNLOAD = "/file/download";
    static final String FILE_DELETE = "/file/delete";

    //등록
    @PostMapping(POST)
    public int saveBoard(@RequestBody BoardPostRequest param) {
        return boardService.saveBoard(param);
    }

    //목록 조회
    @PostMapping(LIST)
    public BoardListResponse getBoardList(@RequestBody BoardListRequest param){
        return boardService.getBoardList(param);
    }

    //상세 조회
    @PostMapping(INFO)
    public BoardDetailResponse getBoard(@RequestBody BoardSeqRequest param){
        return boardService.getBoard(param.getSeq());
    }

    //수정
    @PostMapping(EDIT)
    public void editBoard(@RequestBody BoardEditRequest param) {
        boardService.editBoard(param);
    }

    //삭제
    @PostMapping(DELETE)
    public void deleteBoard(@RequestBody BoardDeleteRequest param) {
        boardService.deleteBoard(param.getSeq());
    }

    //------------------------------------------------------댓글---------------------------------------------------------

    //댓글 등록
    @PostMapping(SAVE_COMMENT)
    public void saveComment(@RequestBody BoardCommentRequest param) {
        boardService.saveComment(param);
    }

    //댓글 조회
    @PostMapping(GET_COMMENT)
    public HashMap<String, List<BoardCommentResponse>> getBoardCommentList(@RequestBody BoardSeqRequest param) {
        HashMap<String, List<BoardCommentResponse>> result = new HashMap<>();
        result.put("boardCommentList", boardService.getBoardCommentList(param.getSeq()));
        return result;
    }

    //댓글 수정
    @PostMapping(EDIT_COMMENT)
    public void editComment(@RequestBody EditCommentRequest param) {
         boardService.editComment(param);
    }

    //댓글 삭제
    @PostMapping(DELETE_COMMENT)
    public void deleteComment(@RequestBody BoardCommentSeqRequest param) {
        boardService.deleteComment(param.getCommentSeq());
    }

    //----------------------------------------------------파일-----------------------------------------------------------

    //게시판 파일 업로드
    @PostMapping(FILE_UPLOAD)
    public void boardFileUpload(@RequestParam("files") List<MultipartFile> files, HttpServletRequest request) throws IOException {
        int seq = Integer.parseInt(request.getHeader("seq"));
        List<FileUploadRequest> fileDataList = FileUtils.FileUpload(files);
        boardService.boardFileData(fileDataList, seq);
    }

    //게시판 파일 다운로드
    @PostMapping(value = FILE_DOWNLOAD, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Object> boardFileDownload(@RequestBody FileRequest file) throws IOException {
        return FileUtils.fileDownload(file);
    }

    //게시판 파일 삭제
    @PostMapping(value = FILE_DELETE)
    public void boardFileDelete(@RequestBody List<FileRequest> fileList) throws IOException {
        for(FileRequest file : fileList){
            if(FileUtils.fileDelete(file)){
                boardService.deleteBoardFile(file);
            }
        }
    }
}
