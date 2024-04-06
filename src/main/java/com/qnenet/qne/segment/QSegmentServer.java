package com.qnenet.qne.segment;//package com.qnenet.qne.segment;

import com.qnenet.qne.objects.classes.QSegmentItem;
import com.qnenet.qne.objects.impl.QNEObjects;
import com.qnenet.qne.system.impl.QNEPaths;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class QSegmentServer {

    private static final int ENTRY_SIZE = 100;
    private static final int SEGMENT_SIZE = 1_000_000;

    @Autowired
    QNEPaths qnePaths;

    @SuppressWarnings("rawtypes")
    @Autowired
    QNEObjects qobjs;

    private Path segmentRafFilePath;
    // private Path segmentNamesRafFilePath;

    private Map<String, Long> segmentNameLookupMap = new ConcurrentHashMap<>();
    private RandomAccessFile segmentRaf;
    private byte[] workBytes = new byte[ENTRY_SIZE];
    // private int segmentId;
    private long baseId;

    @PostConstruct
    public void init(int segmentId) throws IOException {
        // this.segmentId = segmentId;
        baseId = segmentId * SEGMENT_SIZE;
        segmentRaf = new RandomAccessFile(segmentRafFilePath.toFile(), "rws");
        int itemCount = (int) (segmentRaf.length() / ENTRY_SIZE);
        for (int i = 0; i < itemCount; i++) {
            segmentRaf.read(workBytes);
            QSegmentItem segmentItem = (QSegmentItem) qobjs.objFromBytes(workBytes);
            segmentNameLookupMap.put(segmentItem.epName, segmentItem.epAddr.epId.value);
        }
    }

    public void writeSegmentItem(long epIdValue, QSegmentItem segmentItem) throws IOException {
        long itemNumber = epIdValue - baseId;
        byte[] bytes = qobjs.objToBytes(segmentItem);
        segmentRaf.seek(itemNumber * ENTRY_SIZE);
        segmentRaf.write(bytes);
    }

    public QSegmentItem readSegmentItem(long epIdValue) throws IOException {
        segmentRaf.seek((epIdValue - baseId) * ENTRY_SIZE);
        segmentRaf.read(workBytes);
        return (QSegmentItem) qobjs.objFromBytes(workBytes);
    }

    public QSegmentItem getSegmentItemByName(String name) throws IOException {
        Long epIdValue = segmentNameLookupMap.get(name);
        if (epIdValue == null) return null;
        return readSegmentItem(epIdValue);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////
} /////// End Class ///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////


