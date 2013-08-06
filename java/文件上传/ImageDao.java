package com.pufeng.portal.repository;

import com.pufeng.portal.entity.Image;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: Juchen
 * Date: 13-7-8
 * Time: 下午2:37
 */
public interface ImageDao extends PagingAndSortingRepository<Image, Long> {
}
