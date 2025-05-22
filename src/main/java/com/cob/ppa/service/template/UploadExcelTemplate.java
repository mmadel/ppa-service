package com.cob.ppa.service.template;
import com.cob.ppa.entity.ExcelTemplate;
import com.cob.ppa.repository.ExcelTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class UploadExcelTemplate {
    @Autowired
    private ExcelTemplateRepository templateRepository;
    public ExcelTemplate save(MultipartFile file, String templateName, String description) throws IOException {
        ExcelTemplate template = new ExcelTemplate();
        template.setTemplateName(templateName);
        template.setFileContent(file.getBytes());
        template.setFileType(file.getContentType());
        template.setFileSize(file.getSize());
        template.setDescription(description);

        return templateRepository.save(template);
    }
}
