package green_green_avk.anotherterm;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import green_green_avk.libusbmanager.LibUsbManager;

public final class App extends Application {

    public static final class Settings extends green_green_avk.anotherterm.utils.Settings {
        @Param(defRes = R.bool.terminal_use_recents)
        public boolean terminal_use_recents;

        @Param(defRes = R.bool.terminal_font_default_fromfiles)
        public boolean terminal_font_default_fromfiles;

        @Param(defRes = R.integer.terminal_font_default_size_sp)
        public int terminal_font_default_size_sp;

        @Param(defRes = R.integer.terminal_selection_pad_size_dp)
        public int terminal_selection_pad_size_dp;

        @Param(defRes = R.integer.terminal_popup_opacity)
        public int terminal_popup_opacity;

        @Param(defRes = R.integer.terminal_key_height_dp)
        public int terminal_key_height_dp;

        @Param(defRes = R.bool.terminal_key_repeat)
        public boolean terminal_key_repeat;

        @Param(defRes = R.integer.terminal_key_repeat_delay)
        public int terminal_key_repeat_delay;

        @Param(defRes = R.integer.terminal_key_repeat_interval)
        public int terminal_key_repeat_interval;

        @Param(defRes = R.string.terminal_screen_keyboard_default_type)
        public String terminal_screen_keyboard_default_type;

        @Param(defRes = R.string.terminal_ansi_screen_mouse_default_mode)
        public String terminal_ansi_screen_mouse_default_mode;

        @Param(defRes = R.string.terminal_mouse_layout)
        public String terminal_mouse_layout;

        @Param(defRes = R.integer.terminal_scroll_follow_history_threshold)
        public int terminal_scroll_follow_history_threshold;

        @Param(defRes = R.integer.scratchpad_column_width_min_sp)
        public int scratchpad_column_width_min_sp;

        @Param(defRes = R.integer.scratchpad_use_threshold)
        public int scratchpad_use_threshold;

        @Override
        protected void onAfterChange(@NonNull final String key, @Nullable final Object value) {
            if ("terminal_font_default_fromfiles".equals(key)) {
                FontsManager.setFrom(terminal_font_default_fromfiles);
            }
        }

        @Override
        protected void onBeforeInit(@NonNull final SharedPreferences sp) {
            final SharedPreferences.Editor editor = sp.edit();
            try {
                if (!sp.contains("terminal_screen_keyboard_default_type") &&
                        sp.getBoolean("terminal_key_default_ime", false)) {
                    editor.putString("terminal_screen_keyboard_default_type", "ime");
                }
            } catch (final ClassCastException ignored) {
            }
            editor.apply();
        }
    }

    public final Settings settings = new Settings();
//    private static Settings sSettings = null;

//    @Nullable
//    public static Settings getSettings() {
//        return sSettings;
//    }

    public ScratchpadManager scratchpadManager = null;

    public AnsiColorManager ansiColorManager = null;
    public BackgroundsManager backgroundDrawableManager = null;

    // Turned out, it's supposed that any obfuscated fields are reflection unreachable...
    // Must be kept in order to prevent its unexpected early finalization.
    @Keep
    public TermSh termSh = null;

    @Keep
    public LibUsbManager libUsbManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        PasswordService.setContextProvider(ConsoleService::getInstance);
        PluginsManager.init(this);
        settings.init(this, PreferenceManager.getDefaultSharedPreferences(this));
//        sSettings = settings;
        FontsManager.init(this);
        FontsManager.setFrom(settings.terminal_font_default_fromfiles);
        TermKeyMapManager.init(this);
        HwKeyMapManager.init(this);
        FavoritesManager.init(this);
        scratchpadManager = new ScratchpadManager(this, "scratchpad");
        ansiColorManager = new AnsiColorManager(this, "ansi");
        backgroundDrawableManager = new BackgroundsManager(this);
        termSh = new TermSh(this);
        libUsbManager = new LibUsbManager(this);
    }
}
