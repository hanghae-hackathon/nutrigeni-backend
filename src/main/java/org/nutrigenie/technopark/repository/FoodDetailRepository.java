package org.nutrigenie.technopark.repository;

import org.nutrigenie.technopark.model.FoodDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface FoodDetailRepository extends JpaRepository<FoodDetail, Long>, FoodDetailRepositoryCustom {
}
