package software.amazon.ion.impl.lite;

import java.util.HashSet;
import java.util.Set;

public class IonField {

    private final String name;
    private final IonField parent;
    private final Set<IonField> fields = new HashSet<>();

    public IonField(String name, IonField parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public boolean contains(String name) {
        for (IonField field : fields) {
            if (field.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
    public IonField getField(String name) {
        for (IonField field : fields) {
            if (field.name.equals(name)) {
                return field;
            }
        }
        return null;
    }

    public void addFields(Set<IonField> fields) {
        this.fields.addAll(fields);
    }

    public IonField getParent() {
        return parent;
    }

    public Set<IonField> getChild() {
        return fields;
    }
}
