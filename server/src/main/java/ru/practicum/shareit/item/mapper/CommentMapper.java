package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "author.name", target = "authorName")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDto(List<Comment> comments);

    Comment toComment(CommentDto commentDto);

    List<Comment> toComments(List<CommentDto> commentDto);

}
