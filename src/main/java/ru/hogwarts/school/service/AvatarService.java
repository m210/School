package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("avatars")
    private String dir;

    private final StudentService studentService;
    private final AvatarRepository repository;
    private Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(StudentService studentService, AvatarRepository repository) {
        this.studentService = studentService;
        this.repository = repository;
        logger.info("AvatarService is loaded");
    }

    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        logger.info("upload avatar for student with identifier {}", id);
        Student student = studentService.getStudent(id);

        Path filePath = Path.of(dir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try(InputStream is = file.getInputStream();
            OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = findAvatar(id);
        if(avatar == null) {
            avatar = new Avatar();
            logger.info("Create new avatar for student with identifier {}", id);
        } else logger.info("Edit the avatar for student with identifier {}", id);

        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        logger.info("The avatar for student with identifier {} saved", id);
        repository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long id) {
        logger.info("Find an avatar for student with identifier {}", id);
        return repository.findByStudentId(id);
    }

    public List<Avatar> findAll(int number, int size) {
        logger.info("Get all avatars paged by page {} and size {}", number, size);
        return repository.findAll(PageRequest.of(number - 1, size)).getContent();
    }
}
