package ps.poc.paimon;

import org.apache.paimon.catalog.Catalog;
import org.apache.paimon.catalog.Identifier;
import org.apache.paimon.schema.Schema;
import org.apache.paimon.types.DataTypes;

public class CreateTable {

    public static void main(String[] args) {
        Schema.Builder schemaBuilder = Schema.newBuilder();
        schemaBuilder.primaryKey("f0", "f1");
        schemaBuilder.partitionKeys("f1");
        schemaBuilder.column("f0", DataTypes.STRING());
        schemaBuilder.column("f1", DataTypes.INT());
        schemaBuilder.option("bucket", "4");
        Schema schema = schemaBuilder.build();

        Identifier identifier = Identifier.create("my_db", "my_table");
        try {
            Catalog catalog = CreateCatalog.createFilesystemCatalog();

            catalog.createDatabase("my_db", false);

            catalog.createTable(identifier, schema, false);
        } catch (Catalog.TableAlreadyExistException e) {
            e.printStackTrace();
            // do something
        } catch (Catalog.DatabaseNotExistException e) {
            e.printStackTrace();
            // do something
        } catch (Catalog.DatabaseAlreadyExistException e) {
            throw new RuntimeException(e);
        }
    }
}