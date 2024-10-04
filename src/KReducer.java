import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class KReducer extends Reducer<LongWritable, PointWritable, Text, Text> {

    private final Text newCentroidId = new Text();
    private final Text outputValue = new Text();

    @Override
    public void reduce(LongWritable centroidId, Iterable<PointWritable> points, Context context)
            throws IOException, InterruptedException {

        PointWritable ptFinalSum = new PointWritable();
        StringBuilder pointsList = new StringBuilder(); // Để lưu tọa độ các điểm thuộc centroid
        int count = 0;

        for (PointWritable point : points) {
            if (count == 0) {
                ptFinalSum = PointWritable.copy(point);  // Lấy điểm đầu tiên để khởi tạo giá trị sum
            } else {
                ptFinalSum.sum(point);  // Cộng dồn các điểm lại để tính tổng
            }

            pointsList.append(point.toString()).append("; ");  // Lưu lại tọa độ của từng điểm
            count++;
        }

        ptFinalSum.calcAverage();

        newCentroidId.set("Centroid " + centroidId.toString());
        outputValue.set(ptFinalSum.toString() + " | Points: " + pointsList.toString());
        context.write(newCentroidId, outputValue);
    }
}
