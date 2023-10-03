package io.github.caimucheng.leaf.common.scheme;

import android.graphics.Color;

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
        setColor(WHOLE_BACKGROUND, colorScheme.getBackground());
        setColor(LINE_NUMBER_BACKGROUND, colorScheme.getBackground());
        setColor(LINE_NUMBER_CURRENT, colorScheme.getPrimary());
        setColor(LINE_DIVIDER, Color.TRANSPARENT);
        setColor(LINE_NUMBER, colorScheme.getOnBackground());
        setColor(TEXT_NORMAL, colorScheme.getOnBackground());
    }
}
