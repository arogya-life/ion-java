package software.amazon.ion.impl;

import software.amazon.ion.IonSystem;
import software.amazon.ion.IonType;
import software.amazon.ion.SystemSymbols;
import software.amazon.ion.impl.lite.IonField;

public class IonReaderBinaryUserXSelective extends IonReaderBinaryUserX {

    private IonField _current;
    private int hierarchy = 0;
    private int scalarLevel = Integer.MAX_VALUE;

    public IonReaderBinaryUserXSelective(byte[] data, int offset, int length,
                                         IonSystem system, IonField _current) {
        super(system, system.getCatalog(), UnifiedInputStreamX.makeStream(data, offset, length));
        this._current = _current;
    }

    @Override
    public IonType next() {
        IonType type = super.next();

        if (type == null) {
            return null;
        }

        String file_name = getFieldName();

        if (file_name == null || SystemSymbols.SYMBOLS.equals(file_name) || scalarLevel != Integer.MAX_VALUE) {
            return type;
        }

        if (type == IonType.STRUCT || type == IonType.LIST) {
            IonField field = _current.getField(getFieldName());
            if (field != null) {
                this._current = field;
                if (field.getFields().isEmpty()) {
                    scalarLevel = hierarchy;
                }
                return type;
            } else {
                super.stepIn();
                super.stepOut();
            }
            return next();
        } else {
            if (this._current.contains(file_name)) {
                return type;
            } else {
                return next();
            }
        }
    }

    @Override
    public void stepIn() {
        hierarchy = (hierarchy << 1);
        if (getFieldName() != null && !SystemSymbols.SYMBOLS.equals(getFieldName())) {
            hierarchy = hierarchy + 1;
        }
        super.stepIn();
    }

    @Override
    public void stepOut() {
        if ((hierarchy & 1) == 1 && scalarLevel == Integer.MAX_VALUE) {
            this._current = this._current.getParent();
        }
        hierarchy = hierarchy >> 1;
        if (scalarLevel != Integer.MAX_VALUE && scalarLevel == hierarchy) {
            this._current = this._current.getParent();
            scalarLevel = Integer.MAX_VALUE;
        }
        super.stepOut();
    }
}
