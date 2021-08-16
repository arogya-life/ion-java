package software.amazon.ion.impl;

import software.amazon.ion.IonSystem;
import software.amazon.ion.IonType;
import software.amazon.ion.SystemSymbols;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class IonReaderBinaryUserXSelective extends IonReaderBinaryUserX {

    private final Set<String> selection;
    private final LinkedList<String> hierarchy = new LinkedList<>();
    private String fieldName;

    public IonReaderBinaryUserXSelective(byte[] data, int offset, int length,
                                         IonSystem system, Set<String> selection) {
        super(system, system.getCatalog(), UnifiedInputStreamX.makeStream(data, offset, length));
        this.selection = selection;
    }

    @Override
    public IonType next() {
        IonType type = super.next();

        if (type == null) {
            return null;
        }

        fieldName = getFieldName();

        if (fieldName == null || SystemSymbols.SYMBOLS.equals(fieldName)) {
            return type;
        }

        if (type == IonType.STRUCT || type == IonType.LIST) {
            hierarchy.add(fieldName);
            if (selection.contains(hierarchy.stream().filter(Objects::nonNull).collect(Collectors.joining(".")))) {
                hierarchy.removeLast();
                return type;
            } else {
                hierarchy.removeLast();
                super.stepIn();
                super.stepOut();
            }
            return next();
        } else {
            hierarchy.add(fieldName);
            if (selection.contains(hierarchy.stream().filter(Objects::nonNull).collect(Collectors.joining(".")))) {
                hierarchy.removeLast();
                return type;
            } else {
                hierarchy.removeLast();
                return next();
            }
        }
    }

    @Override
    public void stepIn() {
        hierarchy.add(fieldName);
        super.stepIn();
    }

    @Override
    public void stepOut() {
        hierarchy.removeLast();
        super.stepOut();
    }
}
