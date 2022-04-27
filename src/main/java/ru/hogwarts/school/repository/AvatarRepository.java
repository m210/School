package ru.hogwarts.school.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.hogwarts.school.model.Avatar;

public interface AvatarRepository extends PagingAndSortingRepository<Avatar, Long> {
    Avatar findByStudentId(Long studentId);
}
