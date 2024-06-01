package org.nutrigenie.technopark.repository;

import org.nutrigenie.technopark.model.UploadFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadFoodRepository extends JpaRepository<UploadFood, Long>, UploadFoodRepositoryCustom {
}
