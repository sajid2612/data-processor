package com.sajid.processor.dataprocessor.service;

import com.sajid.processor.dataprocessor.IotDataRepository;
import com.sajid.processor.dataprocessor.entity.IotEntity;
import com.sajid.processor.dataprocessor.model.IotData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataProcessorService {
	private final IotDataRepository iotDataRepository;
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


	@Transactional
	public void persistData(List<IotData> dataList) {
		List<IotEntity> iotEntities = toEntities(dataList);
		iotDataRepository.saveAll(iotEntities);
	}

	private List<IotEntity> toEntities(List<IotData> dataList) {
		return dataList.stream().map(iotData -> {
			IotEntity iotEntity = new IotEntity();
			iotEntity.setDeviceId(iotData.getDeviceId());
			iotEntity.setEventTime(LocalDateTime.parse(iotData.getTimestamp(), formatter));
			iotEntity.setPulseRate(iotData.getPatientCondition().getPulseRate());
			iotEntity.setSpO2(iotData.getPatientCondition().getSpO2());
			iotEntity.setBp(iotData.getPatientCondition().getBloodPressure());
			iotEntity.setProcessedTime(LocalDateTime.now());
			return iotEntity;
		}).collect(Collectors.toList());
	}
}
