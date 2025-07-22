package ps.demo.jpademo.service;

import jakarta.transaction.Transactional;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ps.demo.jpademo.config.DemoConstants;
import ps.demo.jpademo.dto.FileChunkRecordDto;
import ps.demo.jpademo.dto.FileResultDto;
import ps.demo.jpademo.entity.ChunkRecord;
import ps.demo.jpademo.entity.FileRecord;
import ps.demo.jpademo.error.DemoClientException;
import ps.demo.jpademo.repository.ChunkRepository;
import ps.demo.jpademo.repository.FileRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FileService {

    public static final Path FILE_STORAGE_LOCATION = Paths.get("upload-folder").toAbsolutePath().normalize();
    //private final Set<Long> receivedChunks = new HashSet<>();

    private final FileRepository fileRepository;
    private final ChunkRepository chunkRepository;

    public FileService(FileRepository fileRepository, ChunkRepository chunkRepository) throws IOException {
        this.fileRepository = fileRepository;
        this.chunkRepository = chunkRepository;
        Files.createDirectories(FILE_STORAGE_LOCATION);
    }


    public boolean isFileUploaded(Long fileRecordId) {
        Optional<FileRecord> fileRecordOptional = fileRepository.findById(fileRecordId);
        if (!fileRecordOptional.isPresent()) {
            return false;
        }
        FileRecord fileRecord = fileRecordOptional.get();
        Set<ChunkRecord> chunkRecords = fileRecord.getChunkRecordSet();
        if (fileRecord.getTotalChunks() != chunkRecords.size()) {
            return false;
        }
        return !chunkRecords.stream().anyMatch(ck -> DemoConstants.UPLOADING.equals(ck.getStatus()));

    }



    @Transactional
    public FileResultDto fileMd5SizeModTimeTypeComparingAndNotExistsCreate(FileChunkRecordDto fcrDto) {
        Optional<FileRecord> fileRecordOptional = fileRepository.findFirstByFileMd5AndFileSizeAndLastModifiedTimeAndFileType(
                fcrDto.getFileMd5(), fcrDto.getFileSize(), fcrDto.getLastModifiedTime(), fcrDto.getFileType());

        FileRecord fileRecord = null;
        if (fileRecordOptional.isPresent()) {
            fileRecord = fileRecordOptional.get();
            return new FileResultDto(fileRecord.getId(), DemoConstants.UPLOADED.equals(fileRecord.getStatus()));
        } else {
            fileRecord = FileRecord.fromDto(fcrDto);
            fileRecord.setPath(UUID.randomUUID().toString());
            fileRecord.setStatus(DemoConstants.UPLOADING);
            fileRecord.setUpdatedAt(LocalDateTime.now());
        }
        FileRecord result = this.fileRepository.save(fileRecord);
        return new FileResultDto(result.getId(), false);
    }

    @Transactional
    public boolean chunkMd5Check(Long fileRecordId, String chunkMd5, Long chunkIndex) {
        Optional<ChunkRecord> chunkRecordOptional = chunkRepository.findByFileRecordIdAndChunkMd5AndChunkIndexAndStatus(fileRecordId, chunkMd5, chunkIndex, DemoConstants.UPLOADED);
        if (chunkRecordOptional.isPresent()) {
            return true;
        }
        return false;
    }


    @Transactional
    public void uploadChunk(Long fileRecordId, String chunkMd5, Long chunkIndex, MultipartFile file) throws IOException {
        FileRecord fileRecord = fileRepository.findById(fileRecordId).get();
        Optional<ChunkRecord> chunkRecordOptional = chunkRepository.findByFileRecordIdAndChunkMd5AndChunkIndex(fileRecordId, chunkMd5, chunkIndex);

        Path fileFolderName = FILE_STORAGE_LOCATION.resolve(fileRecord.getPath());
        String chunkFileName = DemoConstants.PART + chunkIndex;
        Path chunkPath = FILE_STORAGE_LOCATION.resolve(fileFolderName + "/" + chunkFileName);

        ChunkRecord chunkRecord = null;
        if (chunkRecordOptional.isPresent()) {
            chunkRecord = chunkRecordOptional.get();
            if (DemoConstants.UPLOADED.equals(chunkRecord.getStatus())) {
                return;
            }
            Files.deleteIfExists(chunkPath);
        } else {
            chunkRecord = ChunkRecord.builder()
                    .fileRecord(fileRecord)
                    .chunkMd5(chunkMd5)
                    .chunkIndex(chunkIndex)
                    .status(DemoConstants.UPLOADING)
                    .build();
            chunkRepository.save(chunkRecord);
        }

        FileUtils.createParentDirectories(chunkPath.toFile());

        try (InputStream in = file.getInputStream()) {
            Files.copy(in, chunkPath);
        }
        if (!chunkMd5.equals(genMd5(chunkPath.toFile()))) {
            Files.deleteIfExists(chunkPath);
            throw new RuntimeException("File fileRecordId:"+fileRecordId+" chunk index:"+chunkIndex+" mismatch!");
        }
        chunkRecord.setStatus(DemoConstants.UPLOADED);
        chunkRepository.save(chunkRecord);
    }


    private String genMd5(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fis);
        }
    }

    @Transactional
    public boolean changeUploadStatusToUploaded(Long fileId) throws IOException {

        FileRecord fileRecord = fileRepository.findById(fileId)
                .orElseThrow(() -> new DemoClientException("File not found"));
        if (DemoConstants.UPLOADED.equals(fileRecord.getStatus())) {
            return true;
        }
        Set<ChunkRecord> chunkRecords = fileRecord.getChunkRecordSet();
        if (fileRecord.getTotalChunks() != chunkRecords.size()) {
            return false;
        }

        boolean uploading = chunkRecords.stream().anyMatch(ck -> DemoConstants.UPLOADING.equals(
                ck.getStatus()
        ));
        if (uploading) {
            return false;
        }
        int result = fileRepository.updateFileRecordStatus(DemoConstants.UPLOADED, LocalDateTime.now(), fileId, DemoConstants.UPLOADING);
        if (result <= 0) {
            return true;
        }

        Path fileFolderName = FILE_STORAGE_LOCATION.resolve(fileRecord.getPath());
        assumbleFile(fileFolderName.toString(), fileRecord.getTotalChunks());
        return true;
    }


    private void assumbleFile(String fileName, long totalChunks) throws IOException {
        Path filePath = FILE_STORAGE_LOCATION.resolve(fileName);
        try (RandomAccessFile accessFile = new RandomAccessFile(filePath.toFile(), "rw")) {
            for (long i = 0; i < totalChunks; i++) {
                Path chunkPath = FILE_STORAGE_LOCATION.resolve(fileName + DemoConstants.PART + i);
                byte[] chunkData = Files.readAllBytes(chunkPath);
                accessFile.write(chunkData);
                Files.delete(chunkPath);
            }
        }
    }



}
