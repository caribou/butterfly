# butterfly

A social tool for aggregating various social feeds

## Usage

```clj
(require '[butterfly.core :as butterfly])
(butterly/start-streaming {:twitter   {:tag "butterfly" :handler println}
                           :instagram {:tag "butterfly" :handler println}})
```

## License

Copyright © 2013 Instrument

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
