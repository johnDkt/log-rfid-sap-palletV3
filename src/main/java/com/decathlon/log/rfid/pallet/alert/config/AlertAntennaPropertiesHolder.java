package com.decathlon.log.rfid.pallet.alert.config;

import com.decathlon.log.rfid.pallet.alert.message.AlertMessages;
import com.decathlon.log.rfid.pallet.alert.message.AlertPropertiesEnum;
import com.decathlon.log.rfid.pallet.resources.ResourceManager;
import com.decathlon.log.rfid.pallet.utils.NetworkInterfaceHelper;
import lombok.Getter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Collection;
import java.util.List;

@Getter
public class AlertAntennaPropertiesHolder extends AlertPropertiesHolder {

    private String from;
    private List<String> to;
    private String subject;
    private String content;

    public AlertAntennaPropertiesHolder(final DateTime date, final Collection<String> antennas) {
        this.from = parseString(AlertPropertiesEnum.MAIL_FROM) + ".noreply@decathlon.com";
        this.subject = parseString(AlertPropertiesEnum.MAIL_SUBJECT_ALERT_ANTENNA);
        this.to = parsePropertyIntoList(AlertPropertiesEnum.MAIL_TO);
        this.content = createContent(date, antennas);
    }

    private String createContent(final DateTime date, final Collection<String> antennas) {
        final DateTimeFormatter fmtDate = ISODateTimeFormat.date();
        final StringBuilder builder = new StringBuilder();
        final String title = ResourceManager.getInstance()
                .getString("MAIL_TITLE_ALERT_ANTENNA", new String[]{fmtDate.print(date)});

        builder.append("<h3>").append(AlertMessages.getString("MAIL_SUBJECT_ALERT_ANTENNA")).append("</h3>");
        builder.append(title);
        builder.append("<ul>");

        for (String antenna : antennas) {
            builder.append("<li>").append(antenna).append("</li>");
        }

        builder.append("</ul>");
        builder.append("<br/>");

        builder.append("<div style='").append(getIdentityCardStyle()).append("'>");
        builder.append("Warehouse = ").append(AlertMessages.getString("MAIL_WAREHOUSE")).append("<br/>");
        builder.append("Hostname = ").append(AlertMessages.getString("MAIL_HOSTNAME")).append("<br/>");
        builder.append("Description = ").append(ResourceManager.getInstance().getString("MAIL_HOST_DESCRIPTION")).append("<br/>");
        builder.append("Sysinfo = http://").append(new NetworkInterfaceHelper().getIpFromNetworkInterfaces()).append("/sysinfo").append("<br/>");
        builder.append("</div>");

        return builder.toString();
    }

    private String getIdentityCardStyle() {
        final StringBuilder builder = new StringBuilder();
        builder.append("padding: 1em;");
        builder.append("border: 1px solid #aaa;");
        builder.append("border-radius: 3px 3px 3px 3px;");
        builder.append("-webkit-border-radius: 3px 3px 3px 3px;");
        builder.append("background-color: #eee;");
        return builder.toString();
    }


}
