package ru.practicum.mapper.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutputDto;
import ru.practicum.model.compilation.Compilation;

@UtilityClass
public class CompilationMapper {
    public CompilationOutputDto toOutputDto(Compilation compilation) {
        return CompilationOutputDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .build();
    }

    public Compilation toEntity(CompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.getPinned())
                .build();
    }
}
