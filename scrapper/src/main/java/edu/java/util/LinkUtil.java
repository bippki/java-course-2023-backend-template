package edu.java.util;

import edu.java.util.client.BaseClientProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkUtil {
    private final List<BaseClientProcessor> clientProcessors;

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isUrlSupported(URI url) {
        return clientProcessors.stream().anyMatch(processor -> processor.isCandidate(url));
    }
}
