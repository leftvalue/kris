package extensions;

import basetool.ResourceReader;
import basetool.SystemClipboardTools;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/7/27 11:11 PM
 */
public class MailHtmlCreater {
    private static DateTimeFormatter dt_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static DateTimeFormatter d_formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    static String getLastMondayStr() {
        return getLastDayStr(DayOfWeek.MONDAY);
    }

    static String getLastFridayStr() {
        return getLastDayStr(DayOfWeek.FRIDAY);
    }

    static String getLastDayStr(DayOfWeek day) {
        LocalDate today = LocalDate.now();
        LocalDate sameDayLastWeek = today.minusWeeks(1);
        LocalDate target = sameDayLastWeek.with(day);
        return target.format(d_formatter);
    }

    public static class Mail {
        public String author;
        public String title;
        public String start_time;
        public String end_time;
        public String now;


        public Mail(String author, String title) {
            this.author = author;
            this.title = title;
            this.start_time = getLastMondayStr();
            this.end_time = getLastFridayStr();
            this.now = LocalDateTime.now().format(dt_formatter);
        }
    }

    public static class Work {
        public String name = "default name";
        public String detail = "etc.";
        public String progress = "etc.";
        public String start_time = getLastMondayStr();
        public String end_time = getLastFridayStr();
        public String[] responsible_person = new String[]{" "};
        public String progress_summary = "etc.";
        public String nextWeekPlan = "etc.";

        public String getName() {
            return name;
        }

        public Work setName(String name) {
            this.name = name;
            return this;
        }

        public String getDetail() {
            return detail;
        }

        public Work setDetail(String detail) {
            this.detail = detail;
            return this;
        }

        public String getProgress() {
            return progress;
        }

        public Work setProgress(String progress) {
            this.progress = progress;
            return this;
        }

        public String getStart_time() {
            return start_time;
        }

        public Work setStart_time(String start_time) {
            this.start_time = start_time;
            return this;
        }

        public String getEnd_time() {
            return end_time;
        }

        public Work setEnd_time(String end_time) {
            this.end_time = end_time;
            return this;
        }

        public String[] getResponsible_person() {
            return responsible_person;
        }

        public Work setResponsible_person(String[] responsible_person) {
            this.responsible_person = responsible_person;
            return this;
        }

        public String getProgress_summary() {
            return progress_summary;
        }

        public Work setProgress_summary(String progress_summary) {
            this.progress_summary = progress_summary;
            return this;
        }

        public String getNextWeekPlan() {
            return nextWeekPlan;
        }

        public Work setNextWeekPlan(String nextWeekPlan) {
            this.nextWeekPlan = nextWeekPlan;
            return this;
        }
    }

    public String getSimpleTemplate(String author, String title, int lines_count) {
        Work[] ws = new Work[lines_count];
        for (int i = 0; i < lines_count; i++) {
            ws[i] = new Work();
        }
        Mail m = new Mail(author, title);

        String templates_resource_str = new ResourceReader("/templates/mail.stg").read();
        STGroupString group_str = new STGroupString("mail.stg", templates_resource_str, '$', '$');
//        STGroup group = new STGroupFile("/mail.stg", '$', '$');
        ST st = group_str.getInstanceOf("mail");
        st.add("ws", ws);
        st.add("m", m);
        String result = st.render();

        String[] lines = result.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            line = line.replaceAll("^\\s+", "");
            sb.append(line).append("\n");
        }
        result = sb.toString();
        return result;
    }

    public static void main(String[] args) {
        String result = new MailHtmlCreater().getSimpleTemplate("kristendi", "周报", 5);
        System.out.println(SystemClipboardTools.write(result));
    }
}
