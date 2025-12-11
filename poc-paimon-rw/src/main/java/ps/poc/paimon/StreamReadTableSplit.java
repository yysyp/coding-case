package ps.poc.paimon;

import com.google.common.collect.Lists;
import org.apache.paimon.data.InternalRow;
import org.apache.paimon.predicate.Predicate;
import org.apache.paimon.predicate.PredicateBuilder;
import org.apache.paimon.reader.RecordReader;
import org.apache.paimon.table.Table;
import org.apache.paimon.table.source.ReadBuilder;
import org.apache.paimon.table.source.Split;
import org.apache.paimon.table.source.StreamTableScan;
import org.apache.paimon.table.source.TableRead;
import org.apache.paimon.types.DataTypes;
import org.apache.paimon.types.RowType;

import java.util.List;

public class StreamReadTableSplit {

    public static void main(String[] args) throws Exception {
        // 1. Create a ReadBuilder and push filter (`withFilter`)
        // and projection (`withProjection`) if necessary
        Table table = GetTable.getTable();

        PredicateBuilder builder =
                new PredicateBuilder(RowType.of(DataTypes.STRING(), DataTypes.INT()));
        Predicate notNull = builder.isNotNull(0);
        Predicate greaterOrEqual = builder.greaterOrEqual(1, 12);

        int[] projection = new int[]{0, 1};

        ReadBuilder readBuilder =
                table.newReadBuilder()
                        .withProjection(projection)
                        .withFilter(Lists.newArrayList(notNull, greaterOrEqual));

        // 2. Plan splits in 'Coordinator' (or named 'Driver')
        StreamTableScan scan = readBuilder.newStreamScan();
        while (true) {
            List<Split> splits = scan.plan().splits();
            // Distribute these splits to different tasks

            Long state = scan.checkpoint();
            // can be restored in scan.restore(state) after fail over

            // 3. Read a split in task
            TableRead read = readBuilder.newRead();
            RecordReader<InternalRow> reader = read.createReader(splits);

            reader.forEachRemaining( e -> {
                //System.out.println("===>>StreamReadTable.main(): " + e);
                System.out.println("===>>: " + e.getString(0)+", "+e.getInt(1));
            });

            scan.notifyCheckpointComplete(scan.checkpoint());
            Thread.sleep(1000);
        }
    }
}