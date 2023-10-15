package io.github.caimucheng.leaf.common.scheme;


import io.github.caimucheng.leaf.common.model.UnwrappedColorScheme;
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;

public class DynamicEditorColorScheme extends EditorColorScheme {

    private final UnwrappedColorScheme colorScheme;

    public DynamicEditorColorScheme(boolean isDark, UnwrappedColorScheme colorScheme) {
        super(isDark);

        this.colorScheme = colorScheme;
        applyDefault();
    }

    @Override
    public void applyDefault() {
        if (colorScheme == null) {
            return;
        }
        super.applyDefault();
        setColor(WHOLE_BACKGROUND, colorScheme.getWholeBackground());
        setColor(TEXT_NORMAL, colorScheme.getTextNormal());
        setColor(LINE_NUMBER, colorScheme.getLineNumber());
        setColor(LINE_NUMBER_CURRENT, colorScheme.getLineNumberCurrent());
        setColor(LINE_NUMBER_BACKGROUND, colorScheme.getLineNumberBackground());
        setColor(LINE_DIVIDER, colorScheme.getLineDivider());
        setColor(SCROLL_BAR_THUMB, colorScheme.getScrollBarThumb());
        setColor(SCROLL_BAR_TRACK, colorScheme.getScrollBarTrack());
        setColor(SCROLL_BAR_THUMB_PRESSED, colorScheme.getScrollBarThumbPressed());
        setColor(SELECTION_INSERT, colorScheme.getSelectionInsert());
        setColor(SELECTION_HANDLE, colorScheme.getSelectionHandle());
        setColor(TEXT_SELECTED, colorScheme.getTextSelected());
        setColor(CURRENT_LINE, colorScheme.getCurrentLine());
        setColor(SELECTED_TEXT_BACKGROUND, colorScheme.getSelectedTextBackground());
        setColor(LINE_NUMBER_PANEL, colorScheme.getLineNumberPanel());
        setColor(LINE_NUMBER_PANEL_TEXT, colorScheme.getLineNumberPanelText());
    }
}
