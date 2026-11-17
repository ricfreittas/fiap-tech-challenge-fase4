
package br.com.fiap.feedback_api.function;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.TimerTrigger;

import br.com.fiap.feedback_api.service.RelatorioService;

public class RelatorioTimerFunction {

    @FunctionName("relatorioSemanalTimer")
    public void executar(
            @TimerTrigger(
                    name = "timerInfo",
                    schedule = "0 0 12 * * MON"
            ) String timerInfo,
            ExecutionContext context) {

        context.getLogger().info(
                "Timer Trigger do relatorio semanal executado!"

        );
        RelatorioService relatorioService =
                SpringContextProvider.getContext()
                        .getBean(RelatorioService.class);

        context.getLogger().info(
                "RelatorioService carregado com sucesso!"
        );

        var relatorio = relatorioService.gerarRelatorio();

        context.getLogger().info(
                "Relatorio semanal gerado com sucesso!"
        );

        context.getLogger().info(
                "Total de avaliacoes: " + relatorio.totalAvaliacoes()
        );

        context.getLogger().info(
                "Media das notas: " + relatorio.mediaNotas()
        );

        context.getLogger().info(
                "Quantidade por urgencia: " + relatorio.quantidadePorUrgencia()
        );

        context.getLogger().info(
                "Quantidade por dia: " + relatorio.quantidadePorDia()
        );
    }
}
