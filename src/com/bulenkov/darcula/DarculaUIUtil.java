/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bulenkov.darcula;

import com.bulenkov.iconloader.util.*;

import javax.swing.*;
import java.awt.*;

/**
 * @author Konstantin Bulenkov
 */
public class DarculaUIUtil {
  public static final Color GLOW_COLOR = new DoubleColor(new Color(96, 132, 212), new Color(96, 175, 255));
  public static final boolean USE_QUARTZ = "true".equals(System.getProperty("apple.awt.graphics.UseQuartz"));
  public static final String MAC_FILL_BORDER = "MAC_FILL_BORDER";
  public static final int MAC_COMBO_BORDER_V_OFFSET = SystemInfo.isMacOSLion ? 1 : 0;
  private static Cursor INVERTED_TEXT_CURSOR;

  public static void paintFocusRing(Graphics g, int x, int y, int width, int height) {
    paintFocusRing((Graphics2D) g, getGlow(), new Rectangle(x, y, width, height));
  }

  public static void paintFocusOval(Graphics g, int x, int y, int width, int height) {
    paintFocusRing((Graphics2D) g, getGlow(), new Rectangle(x, y, width, height), true);
  }

  private static Color getGlow() {
    return new DoubleColor(new Color(35, 121, 212), new Color(96, 175, 255));
  }

  public static void paintFocusRing(Graphics2D g2d, Color ringColor, Rectangle bounds) {
    paintFocusRing(g2d, ringColor, bounds, false);
  }

  public static void paintFocusRing(Graphics2D g, Color ringColor, Rectangle bounds, boolean oval) {
    int correction = UIUtil.isUnderDarcula() ? 50 : 0;
    final Color[] colors = new Color[]{
        ColorUtil.toAlpha(ringColor, 180 - correction),
        ColorUtil.toAlpha(ringColor, 120 - correction),
        ColorUtil.toAlpha(ringColor, 70  - correction),
        ColorUtil.toAlpha(ringColor, 100 - correction),
        ColorUtil.toAlpha(ringColor, 50  - correction)
    };

    final Object oldAntialiasingValue = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    final Object oldStrokeControlValue = g.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, !oval &&
        USE_QUARTZ ? RenderingHints.VALUE_STROKE_PURE : RenderingHints.VALUE_STROKE_NORMALIZE);


    final Rectangle r = new Rectangle(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);

    g.setColor(colors[0]);
    drawRectOrOval(g, oval, 5, r.x + 2, r.y + 2, r.width - 5, r.height - 5);

    g.setColor(colors[1]);
    drawRectOrOval(g, oval, 7, r.x + 1, r.y + 1, r.width - 3, r.height - 3);

    g.setColor(colors[2]);
    drawRectOrOval(g, oval, 9, r.x, r.y, r.width - 1, r.height - 1);

    g.setColor(colors[3]);
    drawRectOrOval(g, oval, 0, r.x + 3, r.y + 3, r.width - 7, r.height - 7);

    g.setColor(colors[4]);
    drawRectOrOval(g, oval, 0, r.x + 4, r.y + 4, r.width - 9, r.height - 9);

    // restore rendering hints
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasingValue);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, oldStrokeControlValue);
  }

  private static void drawRectOrOval(Graphics2D g, boolean oval, int arc, int x, int y, int width, int height) {
    if (oval) {
      g.drawOval(x, y, width, height);
    } else {
      if (arc == 0) {
        g.drawRect(x, y, width, height);
      } else {
        g.drawRoundRect(x, y, width, height, arc, arc);
      }
    }
  }

  public static void paintSearchFocusRing(Graphics2D g, Rectangle bounds) {
    int correction = UIUtil.isUnderDarcula() ? 50 : 0;
    final Color[] colors = new Color[]{
      ColorUtil.toAlpha(getGlow(), 180 - correction),
      ColorUtil.toAlpha(getGlow(), 120 - correction),
      ColorUtil.toAlpha(getGlow(), 70  - correction),
      ColorUtil.toAlpha(getGlow(), 100 - correction),
      ColorUtil.toAlpha(getGlow(), 50  - correction)
    };

    final Object oldAntialiasingValue = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    final Object oldStrokeControlValue = g.getRenderingHint(RenderingHints.KEY_STROKE_CONTROL);

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, USE_QUARTZ ? RenderingHints.VALUE_STROKE_PURE : RenderingHints.VALUE_STROKE_NORMALIZE);


    final Rectangle r = new Rectangle(bounds.x - 3, bounds.y - 3, bounds.width + 6, bounds.height + 6);

    g.setColor(colors[0]);
    g.drawRoundRect(r.x + 2, r.y + 2, r.width - 5, r.height - 5, r.height - 5, r.height - 5);

    g.setColor(colors[1]);
    g.drawRoundRect(r.x + 1, r.y + 1, r.width - 3, r.height - 3, r.height - 3, r.height - 3);

    g.setColor(colors[2]);
    g.drawRoundRect(r.x, r.y, r.width - 1, r.height - 1, r.height - 1, r.height - 1);


    g.setColor(colors[3]);
    g.drawRoundRect(r.x+3, r.y+3, r.width - 7, r.height - 7, r.height - 7, r.height - 7);

    g.setColor(colors[4]);
    g.drawRoundRect(r.x+4, r.y+4, r.width - 9, r.height - 9, r.height - 9, r.height - 9);

    // restore rendering hints
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasingValue);
    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, oldStrokeControlValue);
  }

  public static Icon getTreeNodeIcon(boolean expanded, boolean selected, boolean focused) {
    boolean white = (selected && focused) || UIUtil.isUnderDarcula();

    Icon selectedIcon = getTreeSelectedExpandedIcon();
    Icon notSelectedIcon = getTreeExpandedIcon();

    int width = Math.max(selectedIcon.getIconWidth(), notSelectedIcon.getIconWidth());
    int height = Math.max(selectedIcon.getIconWidth(), notSelectedIcon.getIconWidth());

    return new CenteredIcon(expanded ? (white ? getTreeSelectedExpandedIcon() : getTreeExpandedIcon())
        : (white ? getTreeSelectedCollapsedIcon() : getTreeCollapsedIcon()),
        width, height, false
    );
  }

  public static Icon getTreeCollapsedIcon() {
    return UIManager.getIcon("Tree.collapsedIcon");
  }

  public static Icon getTreeExpandedIcon() {
    return UIManager.getIcon("Tree.expandedIcon");
  }

  public static Icon getTreeSelectedCollapsedIcon() {
    return getTreeCollapsedIcon();
  }

  public static Icon getTreeSelectedExpandedIcon() {
    return getTreeExpandedIcon();
  }

}
