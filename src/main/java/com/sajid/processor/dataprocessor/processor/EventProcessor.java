package com.sajid.processor.dataprocessor.processor;

import org.springframework.stereotype.Service;

@Service
public interface EventProcessor {
	void consumeEvents(int batchSize);
}
