package com.sajid.processor.dataprocessor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajid.processor.dataprocessor.model.IotData;
import com.sajid.processor.dataprocessor.service.DataProcessorService;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlatFileProcessor implements EventProcessor {

	private final DataProcessorService dataProcessorService;
	static ObjectMapper obm = new ObjectMapper();

	@Override
	public void consumeEvents(int batchSize) {
		String filename = "data-stream.text";
		String workingDirectory = System.getProperty("user.dir");
		System.out.println("Processing data...");
		prepareFile(new File(workingDirectory, filename));
		long position = 0;
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(workingDirectory + File.separator + filename, "r");
			ArrayList<IotData> dataList = new ArrayList<>();
			while (true) {
				dataList.clear();

				raf.seek(position);
				String str;
				int capacity = 0;
				while ((str = raf.readLine()) != null && capacity < 5) {
					IotData iotData = parse(str);
					dataList.add(iotData);
					System.out.println(str);
					capacity ++;
					position = raf.getFilePointer();
				}
				dataProcessorService.persistData(dataList);
				Thread.sleep(batchSize);
			}
		} catch (IOException | InterruptedException e) {
			//TODO(Limitation for now) : Those failed events can be captured in another file, lets say data-process-failed, which can be further processed by same/other system
			e.printStackTrace();
		}
	}

	private static IotData parse(String line) {
		try {
			return obm.readValue(line, IotData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void prepareFile(File file)  {
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File is already existed!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
