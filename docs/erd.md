```mermaid
erDiagram
    user ||--o{ watchlist : has
    user ||--o{ favorites : has
    
    watchlist }o--o{ movie : contains
    watchlist }o--o{ series : contains
    
    favorites }o--o{ movie : contains
    favorites }o--o{ series : contains
    
    series ||--o{ season : has
    season ||--o{ episode : has
```
