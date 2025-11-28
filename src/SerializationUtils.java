import java.io.*;

/**
 * Utility class that provides deep-copying of Serializable objects
 * using Java's built-in serialization mechanism.
 *
 * This is used by the undo/redo system to create independent
 * snapshots of game state without manually copying every field.
 */
public class SerializationUtils {

    /**
     * Creates a deep copy of the given Serializable object.
     * The object is serialized into a byte array and then
     * deserialized back into a new instance.
     *
     * @param object the Serializable object to clone
     * @param <T> the type of object being cloned
     * @return a deep copy of the object
     * @throws RuntimeException if cloning fails due to an I/O error
     *                          or a missing class definition
     */
    public static <T extends Serializable> T clone(T object) {
        try {
            // Write object to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();

            byte[] bytes = bos.toByteArray();

            // Read object back from byte array
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(bis);

            return (T) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Serialization clone failed", e);
        }
    }
}
