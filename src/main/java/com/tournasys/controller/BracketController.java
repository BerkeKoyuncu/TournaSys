package com.tournasys.controller;

import java.util.List;

import com.tournasys.model.Match;
import com.tournasys.model.Tournament;
import com.tournasys.service.MatchService;
import com.tournasys.service.TournamentService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BracketController {

    @FXML private NavbarController navbarController;
    @FXML private ComboBox<Tournament> tournamentBox;
    @FXML private HBox bracketContainer;
    @FXML private Label winnerLabel;

    private final TournamentService tournamentService = new TournamentService();
    private final MatchService matchService = new MatchService();

    @FXML
    public void initialize() {
        if (navbarController != null) {
            navbarController.setActivePage("bracket");
        }

        setupTournamentBox();
        tournamentBox.setItems(FXCollections.observableArrayList(tournamentService.getAllTournaments()));
        tournamentBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTournament, newTournament) -> renderSelectedTournament()
        );

        if (!tournamentBox.getItems().isEmpty()) {
            tournamentBox.getSelectionModel().selectFirst();
        }
    }

    private void renderSelectedTournament() {
        bracketContainer.getChildren().clear();

        Tournament tournament = tournamentBox.getValue();
        if (tournament == null) {
            winnerLabel.setText("Winner: Not decided yet");
            addMessage("Select a tournament to view its bracket.");
            return;
        }

        List<Match> matches = matchService.getMatchesByTournament(tournament.getTournamentId());
        if (matches.isEmpty()) {
            winnerLabel.setText("Winner: Not decided yet");
            addMessage("No schedule has been generated for this tournament yet.");
            return;
        }

        winnerLabel.setText(getTournamentWinnerText(tournament, matches));

        if ("Knockout".equalsIgnoreCase(tournament.getType())) {
            renderKnockout(tournament, matches);
        } else {
            renderLeague(matches);
        }
    }

    private void renderKnockout(Tournament tournament, List<Match> matches) {
        int roundSize = Math.max(1, tournamentService.getTournamentById(tournament.getTournamentId()).getTeams().size() / 2);
        int index = 0;
        int roundNumber = 1;

        while (index < matches.size() && roundSize > 0) {
            int end = Math.min(index + roundSize, matches.size());
            addRoundColumn(getRoundTitle(roundNumber, roundSize), matches.subList(index, end));
            index = end;
            roundSize /= 2;
            roundNumber++;
        }
    }

    private void renderLeague(List<Match> matches) {
        for (int i = 0; i < matches.size(); i++) {
            addRoundColumn("Round " + (i + 1), List.of(matches.get(i)));
        }
    }

    private void addRoundColumn(String title, List<Match> matches) {
        VBox column = new VBox(14);
        column.setAlignment(Pos.TOP_CENTER);
        column.setMinWidth(210);
        column.getStyleClass().add("bracket-round");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("bracket-round-title");
        column.getChildren().add(titleLabel);

        for (Match match : matches) {
            column.getChildren().add(createMatchCard(match));
        }

        bracketContainer.getChildren().add(column);
    }

    private VBox createMatchCard(Match match) {
        VBox card = new VBox(6);
        card.getStyleClass().add("bracket-match-card");

        Label home = new Label(formatTeam(match, true));
        Label away = new Label(formatTeam(match, false));
        Label status = new Label(match.getStatus());

        home.getStyleClass().add("bracket-team");
        away.getStyleClass().add("bracket-team");
        status.getStyleClass().add("bracket-status");

        card.getChildren().addAll(home, away, status);

        if (match.isCompleted()) {
            Label winner = new Label(formatWinner(match));
            winner.getStyleClass().add("bracket-winner");
            card.getChildren().add(winner);
        }

        return card;
    }

    private String formatTeam(Match match, boolean homeTeam) {
        String name = homeTeam ? match.getHomeTeam().getName() : match.getAwayTeam().getName();
        if (!match.isCompleted()) {
            return name + "  -";
        }

        int score = homeTeam ? match.getHomeScore() : match.getAwayScore();
        return name + "  " + score;
    }

    private String formatWinner(Match match) {
        if (match.isDraw()) {
            return "Winner: Draw";
        }

        return "Winner: " + match.getWinner().getName();
    }

    private String getTournamentWinnerText(Tournament tournament, List<Match> matches) {
        boolean allCompleted = matches.stream().allMatch(Match::isCompleted);
        if (!allCompleted) {
            return "Winner: Not decided yet";
        }

        if ("Knockout".equalsIgnoreCase(tournament.getType())) {
            Match finalMatch = matches.get(matches.size() - 1);
            if (finalMatch.getWinner() == null) {
                return "Winner: Not decided yet";
            }

            return "Tournament Winner: " + finalMatch.getWinner().getName();
        }

        Tournament loadedTournament = tournamentService.getTournamentById(tournament.getTournamentId());
        loadedTournament.updateStandings();
        if (loadedTournament.getStandings().getLeader() == null) {
            return "Winner: Not decided yet";
        }

        return "Tournament Winner: " + loadedTournament.getStandings().getLeader().getTeam().getName();
    }

    private String getRoundTitle(int roundNumber, int roundSize) {
        if (roundSize == 1) {
            return "Final";
        }

        return "Round " + roundNumber;
    }

    private void addMessage(String message) {
        Label label = new Label(message);
        label.getStyleClass().add("page-subtitle");
        bracketContainer.getChildren().add(label);
    }

    private void setupTournamentBox() {
        tournamentBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatTournament(item));
            }
        });

        tournamentBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatTournament(item));
            }
        });
    }

    private String formatTournament(Tournament tournament) {
        return tournament.getName() + " - " + tournament.getType() + " - " + tournament.getStatus();
    }
}
