package com.pufeng.portal.service.admin;

import com.pufeng.portal.entity.Image;
import com.pufeng.portal.repository.ImageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Juchen
 * Date: 13-7-8
 * Time: 下午2:37
 */
@Component
@Transactional (readOnly = true)
public class ImageService {

    private ImageDao imageDao;

    @Transactional (readOnly = false)
    public void save(Image image){
        imageDao.save(image);
    }

    public Image findById(Long id){
        return imageDao.findOne(id);
    }

    @Transactional (readOnly = false)
    public void delete(Long id){
        imageDao.delete(id);
    }

    @Autowired
    public void setImageDao(ImageDao imageDao) {
        this.imageDao = imageDao;
    }
}
