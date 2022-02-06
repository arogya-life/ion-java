package software.amazon.ion.impl.lite;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class IonField implements Serializable {

    private String name;
    private IonField parent;
    private Set<IonField> fields = new HashSet<>();

    public IonField() {

    }

    public IonField(String name, IonField parent) {
        this.name = name;
        this.parent = parent;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IonField getParent() {
        return parent;
    }

    public void setParent(IonField parent) {
        this.parent = parent;
    }

    public Set<IonField> getFields() {
        return fields;
    }

    public void setFields(Set<IonField> fields) {
        this.fields = fields;
    }
}
