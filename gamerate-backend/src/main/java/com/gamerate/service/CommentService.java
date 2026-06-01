package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.common.result.PageResult;
import com.gamerate.dto.CommentSubmitDTO;
import com.gamerate.entity.GameComment;
import com.gamerate.vo.GameCommentVO;

public interface CommentService extends IService<GameComment> {

    GameCommentVO submitComment(CommentSubmitDTO submitDTO);

    void deleteComment(Long commentId);

    PageResult<GameCommentVO> pageGameComments(Long gameId, Integer pageNum, Integer pageSize);
}
