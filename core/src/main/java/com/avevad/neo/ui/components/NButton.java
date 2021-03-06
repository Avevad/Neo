package com.avevad.neo.ui.components;

import com.avevad.neo.graphics.*;
import com.avevad.neo.ui.NComponent;
import com.avevad.neo.ui.NEvent;
import com.avevad.neo.ui.NEventDispatcher;
import com.avevad.neo.ui.NHorizontalDirection;
import com.avevad.neo.ui.events.*;

public class NButton extends NComponent {
    public static final NKeyEvent.NKey EMULATE_MOUSE_PRESS_KEY = NKeyEvent.NKey.SPACE;
    public static final NKeyEvent.NKey EMULATE_MOUSE_CLICK_KEY = NKeyEvent.NKey.ENTER;

    private NImage icon;
    private NHorizontalDirection iconPosition;
    private String text = "";
    private NFont font;
    private int backgroundColor = NColor.NONE;
    private int foregroundColor = NColor.NONE;
    protected boolean isPressed = false;
    protected boolean isHovered = false;
    private NButtonUI ui;

    public final NEventDispatcher<ClickedEvent> clicked = new NEventDispatcher<>();
    private boolean enabled = true;

    public void setUI(NButtonUI ui) {
        this.ui = ui;
    }

    public NButtonUI getUI() {
        return ui;
    }

    public void setIcon(NImage icon) {
        this.icon = icon;
    }

    public NImage getIcon() {
        return icon;
    }

    public void setIconPosition(NHorizontalDirection iconPosition) {
        this.iconPosition = iconPosition;
    }

    public NHorizontalDirection getIconPosition() {
        return iconPosition;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setFont(NFont font) {
        this.font = font;
    }

    public NFont getFont() {
        return font;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void resizeToFit() {
        setSize(NLabel.resizeToFit(icon, text, getGraphics().getFontMetrics(font)));
    }

    @Override
    public boolean onMousePressed(NMousePressedEvent event) {
        if (!isEnabled()) return false;
        isPressed = new NRectangle(NPoint.ZERO, getSize()).contains(event.x, event.y);
        update();
        return isPressed;
    }

    @Override
    public boolean onMouseReleased(NMouseReleasedEvent event) {
        if (!isEnabled()) return false;
        boolean ret = new NRectangle(NPoint.ZERO, getSize()).contains(event.x, event.y);
        boolean isClicked = isPressed && ret;
        isPressed = false;
        update();
        if (isClicked) clicked.trigger(new ClickedEvent());
        return ret;
    }

    @Override
    public boolean onMouseDragged(NMouseDraggedEvent event) {
        if (!isEnabled()) return false;
        isHovered = new NRectangle(NPoint.ZERO, getSize()).contains(event.x, event.y);
        update();
        return isHovered;
    }

    @Override
    public boolean onMouseWheelScrolled(NMouseWheelScrolledEvent event) {
        if (!isEnabled()) return false;
        return new NRectangle(NPoint.ZERO, getSize()).contains(event.x, event.y);
    }

    @Override
    public boolean onMouseMoved(NMouseMovedEvent event) {
        if (!isEnabled()) return false;
        isHovered = new NRectangle(NPoint.ZERO, getSize()).contains(event.x, event.y);
        update();
        return isHovered;
    }

    @Override
    public void onMouseExited() {
        isHovered = false;
        update();
    }

    @Override
    public void onKeyPressed(NKeyPressedEvent event) {
        if (!isEnabled()) return;
        if (event.key == EMULATE_MOUSE_PRESS_KEY) {
            isPressed = true;
        }
        if (event.key == EMULATE_MOUSE_CLICK_KEY) {
            isPressed = !isPressed;
            if (isPressed) clicked.trigger(new ClickedEvent());
        }
        update();
    }

    @Override
    public void onKeyReleased(NKeyReleasedEvent event) {
        if (!isEnabled()) return;
        if (event.key == EMULATE_MOUSE_CLICK_KEY || event.key == EMULATE_MOUSE_PRESS_KEY) {
            if (isPressed && event.key != EMULATE_MOUSE_CLICK_KEY) clicked.trigger(new ClickedEvent());
            isPressed = false;
        }
        update();
    }

    @Override
    public boolean isKeyboardNeeded() {
        return false;
    }

    @Override
    public boolean render(int layer, NRectangle area) {
        return ui.drawButton(this, layer, area);
    }

    public final boolean isEnabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public interface NButtonUI {
        boolean drawButton(NButton button, int layer, NRectangle area);
    }

    public static class ClickedEvent extends NEvent {

    }
}
