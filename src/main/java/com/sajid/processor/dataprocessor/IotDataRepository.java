package com.sajid.processor.dataprocessor;

import com.sajid.processor.dataprocessor.entity.IotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface IotDataRepository extends JpaRepository<IotEntity, String>, JpaSpecificationExecutor<IotEntity> {
}
