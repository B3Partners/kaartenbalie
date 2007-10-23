SELECT * FROM layer l WHERE l.serviceproviderid =
    (SELECT serviceproviderid FROM layer WHERE parentid IS NULL GROUP BY serviceproviderid HAVING COUNT(*) > 1)