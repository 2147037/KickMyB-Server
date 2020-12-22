package org.kickmyb.server.photo;

import org.kickmyb.server.model.MTask;
import org.kickmyb.server.model.MPicture;
import org.kickmyb.server.model.MPictureRepository;
import org.kickmyb.server.model.MTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service // Peut être remplacé par @Component mais c'est juste plus clair. Indique qu'il s'agit d'un bean, permet l'injection dans Autowired
public class ServicePicture {

    // Spring va automatiquement chercher la classe annotée @Component ou @Repository qui correspond à ce type
    @Autowired private MPictureRepository repoPics;
    @Autowired private MTaskRepository repo;

    // TODO show what happens when transactional is removed by throwing an exception between writes??
    @Transactional
    public MPicture store(MultipartFile file, Long elementID) throws IOException {
        //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        MTask element = repo.findById(elementID).get();
        try{
            MPicture existing = repoPics.findByTask(element).get();
            repoPics.delete(existing);
        } catch(Exception e){}
        // throw an exception here to show that trabsactional protects against delete but not store
        MPicture babyPicture = new MPicture();
        babyPicture.blob = file.getBytes();
        babyPicture.contentType = file.getContentType();
        babyPicture.task = element;
        return repoPics.save(babyPicture);
    }

    public MPicture getFile(Long id) {
        return repoPics.findById(id).get();
    }

    public MPicture getFileForBaby(Long elementID) {
        MTask element = repo.findById(elementID).get();
        return repoPics.findByTask(element).get();
    }
}