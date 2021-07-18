package poc.application.systemManage.command;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import poc.application.common.command.BaseCommand;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(callSuper=false)
public class KzdySourceFindCommand extends BaseCommand {

   private String kzdyId;

    public KzdySourceFindCommand(String KzdyId,
                                 String operator,
                                 LocalDateTime triggerTime) {
        super(operator, triggerTime);
        this.kzdyId = KzdyId;
    }

    public KzdySourceFindCommand(String operator,
                                 LocalDateTime triggerTime) {
        super(operator, triggerTime);
    }

}
